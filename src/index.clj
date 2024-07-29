(ns index
  (:require [tablecloth.api :as tc]
            [scicloj.noj.v1.vis.hanami :as hanami]
            [aerial.hanami.templates :as ht]
            [scicloj.kindly.v4.kind :as kind]))

;; # A study on layoffs in tech, covering 2023 and 2024

;; Welcome reader,

;; [layoffs.fyi](https://layoffs.fyi/) is a site that provides an [Airtable](https://www.airtable.com/) showing layoffs in big tech.

;; Big tech in this domain is anything FAANG and sub-FAANG.

;; The data is mostly from US companies or companies overseas that do have a US headquarter.

;; ### Methodology

;; I scraped off the airtable from layoffs.fyi onto a .csv you can find on the repo
;; containing this study. The dataset itself is limited to the first 1,000 entries
;; due to limitations with the scraping tool I used.

;; The data itself, as a whole, can be made available to me such that this study
;; self updates by way of a recurring job. But that can only be achieved if the owner
;; of the Airtable grants me the access to.

;; If you are one of the authors of layoffs.fyi, I'd **really** like to speak with you. It's a really hard time in tech and we, as a community, need to build the monitors in place so situations like this don't catch up to us.

;; As for a tech stack, I decided to use a mash up between [scicloj](https://scicloj.github.io/) + [quarto](https://quarto.org/)

;; ### Results
