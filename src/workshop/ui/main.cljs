(ns workshop.ui.main
  (:require [keechma.next.helix.core :refer [with-keechma use-sub]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <>]]
            ["react" :as react]
            ["react-dom" :as rdom]
            [helix.dom :as d]))

(defnc MainRenderer
  [props]
       (<>
         (d/a {:href (router/get-url props :router {:page "inbox"})} "inbox")
         (d/a {:href (router/get-url props :router {:page "inbox" :subpage "1"})} "inbox poruka")
         (d/h1 "Hello VBTT")))

(def Main (with-keechma MainRenderer))