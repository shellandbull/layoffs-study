(ns index
  (:require [tablecloth.api :as tc]
            [scicloj.noj.v1.vis.hanami :as hanami]
            [aerial.hanami.templates :as ht]
            [scicloj.kindly.v4.kind :as kind]))

;; # A study on layoffs in tech 2024

;; Welcome reader,

;; [layoffs.fyi](https://layoffs.fyi/) is a handy site that provides an [Airtable](https://www.airtable.com/) view showcasing layoffs in tech.
;; The data is mostly US based but I hope that it will grow in time

;; This site does a small statistical exploration on the dataset provided

;; I used a small python script to do a .pdf export which I then converted to .csv

;; I am open to receive contributions for something better as ideally we could connect this to a CI and have it self-update every day or so.
