(ns notebooks.layoffs
  (:require
   [clojure.string :as str]
   [tablecloth.api :as tc]
   [tablecloth.column.api :as tcc]
   [scicloj.metamorph.ml.toydata :as data]
   [scicloj.kindly.v4.kind :as kind]))

;; # Layoffs in 2024

;; 2023 and 2024 have been really hard for tech. I have scraped the data from layoffs.fyi using [coupler.io](https://www.coupler.io/) which only limits me to the first 1,000 rows as I am using the freemium.

;; If you happen to know the OP of layoffs.fyi please show them this study. If we had a live integration with Airtable
;; this could become a live study where we track the pulse of layoffs in tech.

;; I am using the [scicloj](https://scicloj.github.io/) stack for this study.

;; Without furder ado. Here's an analysis of tech layoffs in 2024

(defn to-kebab-case [s]
  "replaces whitespaces in a string for the character \"-\" lowercases the entire string"
  (-> s
      (str/lower-case)
      (str/replace #" " "-")))

(def layoffs
  (-> "./data/layoffs.csv"
      (tc/dataset {:key-fn (comp keyword to-kebab-case)})))

(tc/info layoffs :basic)

(tc/info layoffs)

;; ## What countries where the most affected?

(def layoffs-by-country
  (-> layoffs
      (tc/group-by :country)
      (tc/aggregate {:total-layoffs (fn [ds]
                                      (-> ds
                                          :#-laid-off
                                          tcc/sum))})
      (tc/order-by :total-layoffs :desc)))

(def plotly-layoffs-by-country
  {:data [{
           :x    (:$group-name layoffs-by-country)
           :y    (:total-layoffs layoffs-by-country)
           :type :bar
           :marker {:size 4 :colorscale :Viridis}}]
   :layout {
            :xaxis {:title "Countries" :type :category}
            :yaxis {:title "People laid off" :type :log}
            :title "Layoffs by country"}})

;; The top 3 countries that were the most affected by reported mass layoffs in tech were:

;; - United States
;; - India
;; - United Kingdom

(tc/head layoffs-by-country 3)

;; The following chart has a logarithmic scale applied to it so be sure to hover on each bar to have a clear number
;; of layoffs over the desired country

(kind/plotly plotly-layoffs-by-country)

;; It makes perfect sense for the chart to look like this as it seems to follow a pattern where
;; _the larger the tech economy, the harder the layoffs_. However this is just a hypothesis

;; To formulate an answer to the question we first need to get a hold of an estimated size of the tech sector for each country
;; There are however, a few pitfalls:

;; - The "tech" industry may encompass different businesses from country to country
;; - There is no objective metrics for errors of measure across each entry
;; - Layoffs can be attributed to a transnational decision. E,g: a company with an HQ in country A may opt for mass layoffs in it's office located in country B. But this study is attributing the layoffs to country B, even though the decision may come from the same company, located elsewhere.
