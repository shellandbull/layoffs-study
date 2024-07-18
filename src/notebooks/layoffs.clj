(ns notebooks.layoffs
  (:require
   [clojure.string :as str]
   [tablecloth.api :as tc]
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

;; ## What areas where the most affected?

(tc/column-names layoffs)

(defn tc-sum-by [column])

(-> layoffs
    (tc/group-by :country)
    (tc/add-column :number-of-layoffs-by-country (sum-by :#-laid-off))
    )
