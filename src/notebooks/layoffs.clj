(ns notebooks.layoffs
  (:require
   [clojure.string :as str]
   [tablecloth.api :as tc]
   [tablecloth.column.api :as tcc]
   [scicloj.metamorph.ml.toydata :as data]
   [scicloj.kindly.v4.kind :as kind]))

;; # Layoffs in 2024

;; 2023 and 2024 have been hard for tech. I have scraped the layoff data from layoffs.fyi using [coupler.io](https://www.coupler.io/).

;; I am currently limited to the first 1,000 rows but I hope that will change once I gain a few donors for the project.

;; If you know the author of [layoffs.fyi](https://layoffs.fyi) please pass them the study. A live integration with Airtable we could essentially have a live monitor for layoffs in tech globally. A first of it's kind.

;; Without further ado. Here's an analysis of tech layoffs in 2024

(defn to-kebab-case [s]
  "replaces whitespaces in a string for the character \"-\" lowercases the entire string"
  (-> s
      (str/lower-case)
      (str/replace #" " "-")))

(def layoffs
  (-> "./data/layoffs.csv"
      (tc/dataset {:key-fn (comp keyword to-kebab-case)})))

;; What type of information is available? Here's a list of the information currently available on layoffs.fyi

(kind/table (tc/head layoffs 1))

;; - Company
;; - Location of the headquarters(HQ)
;; - Number of people laid off
;; - Date of the event
;; - Industry
;; - Source. Often a link from a major news outlet. This means we exclude quiet layoffs
;; - List of employees laid off
;; - Stage. Refers to investment stage. e,g: seed, series, IPO, etc.
;; - Funding raised. Expressed in $USD in the order of millions
;; - Country
;; - Date added

;; # Overview

;; This study is fairly simple. The number of layoffs is going to grouped by other criteria exposed in the dataset such as country, HQ, industry, funding

;; If the study gains enough traction there's a list of additions worth exploring, such as:

;; - [ ] Join the dataset by country with a dataset from [IMF](https://www.imf.org/en/Data) to gain access to inflation rate, GDP, overall unemployment, AI readiness, etc.
;; - Richer data visualisations using [Hanami](https://github.com/jsa-aerial/hanami)
;; - [ ] Live integration with Airtable so the data self-updates and we have a living, breathing monitor of layoffs in tech
;; - [ ] Machine learning applications to predict what industry comes next. We'd need a whole bunch of data for this
;; - [ ] Join the dataset by a company's lead set of investors
;; - [ ] Have a dataset of investors. Perhaps there's an overlap between lead investors, industry & layoff frequency?

;; ## By country

;; The top 3 countries that were the most affected by reported mass layoffs in tech were:

;; - United States
;; - India
;; - United Kingdom

(defn layoffs-by [ds coll]
  "Groups a dataset by the value of coll and adds a new column :total-layoffs which is a tcc/sum of group.#-laid-off"
  (-> layoffs
      (tc/group-by coll)
      (tc/aggregate {:total-layoffs (fn [ds]
                                      (-> ds
                                          :#-laid-off
                                          tcc/sum))})
      (tc/order-by :total-layoffs :desc)))

(def layoffs-by-country
  (layoffs-by layoffs :country))

(defn plotly-layoffs-by [ds y-axis-title title]
  {:data [{
           :x    (:$group-name ds)
           :y    (:total-layoffs ds)
           :type :bar
           :marker {:size 4 :colorscale :Viridis}}]
   :layout {
            :xaxis {:title y-axis-title :type :category}
            :yaxis {:title "People laid off" :type :log}
            :title title}})

(-> layoffs-by-country
    (tc/head 3)
    (kind/table))

;; The recession is pretty global but an industry like tech has always been very connected the G7. Add to that the common location for back offices, nearshoring & offshoring and you get very predictable groupings

;; The following chart has a logarithmic scale applied to it so be sure to hover on each bar to have a clear number
;; of layoffs over the desired country

(kind/plotly
 (plotly-layoffs-by (layoffs-by layoffs :country) "Countries" "Layoffs by country"))

;; It makes perfect sense for the chart to look like this as it seems to follow a pattern where
;; _the larger the tech economy, the harder the layoffs_. However this is just a hypothesis

;; ### The larger the tech economy, the more layoffs?

;; There are a few caveats with attempting to overlay the size of an economy, the size of it's tech sector and the volume of the country's layoffs within the tech industry:

;; - The "tech" industry is an umbrella term. The tech industry may encompass different sub industries from country to country. Layoffs.fyi mostly has layoff data for the

;; - There is no objective metrics for errors of measure across each entry

;; - Layoffs can be attributed to a transnational decision. E,g: a company with an HQ in country A may opt for mass layoffs in it's office located in country B. But this study is attributing the layoffs to country B, even though the decision may come from the same company, located elsewhere.

;; ## Where are the HQs located, by number of layoffs?

(def layoffs-by-location-hq (layoffs-by layoffs :location-hq))

(kind/plotly
 (plotly-layoffs-by (tc/head layoffs-by-location-hq 50) "Location HQ" "Layoffs by location HQ"))

;; ## By Industry

(def layoffs-by-industry (layoffs-by layoffs :industry))

(tc/head layoffs-by-industry 10)

(kind/plotly
 (plotly-layoffs-by (tc/head layoffs-by-industry 50) "Industry" "Layoffs by industry"))

;; Transport, finance, retail & consumer tech were the most affected. These are industries directly affected by the wave
;; of remote work too.

;; Working from home means a worker is less likely to spend in transportation, the high street or even plain consumer tech.

;; Finance stands out but it's an industry known for layoffs in mass. It doesn't come across as a surprise. It's a boom and bust industry.

;; ## By amount of funding raised

;; TODO How to take the dataset and add a new column
;; where the cell values are a valid range for the amount of funding a company may have


(kind/plotly
 {:data [{
          :x    (:$-raised-mm layoffs)
          :y    (:total-layoffs layoffs)
          :nbinsx 50
          :type :bar
          :marker {:size 4 :colorscale :Viridis}}]
  :layout {
           :xaxis {:title "Amount of funding" :type :category}
           :yaxis {:title "People laid off"}
           :title "Layoffs by funding"}})
