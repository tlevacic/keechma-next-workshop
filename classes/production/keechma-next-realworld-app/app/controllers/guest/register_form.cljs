(ns app.controllers.guest.register-form
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.next.toolbox.pipeline :refer [pswap! preset!] :refer-macros [pipeline!]]
            [app.api :as api]
            [promesa.core :as p]
            [keechma.next.controllers.form :as form]
            [app.validators :as v]
            [promesa.core :as p]))

(derive :guest/register-form ::pipelines/controller)

(def pipelines
  {:keechma.form/submit-data (pipeline! [value ctrl]
                               (api/register value)
                               (ctrl/broadcast ctrl :guest/login value)
                               (ctrl/dispatch ctrl :router :redirect! {:page "home" :subpage "personal"}))})

(defmethod ctrl/prep :guest/register-form [ctrl]
  (pipelines/register ctrl (form/wrap pipelines (v/to-validator {:username [:not-empty]
                                                                 :email [:email :not-empty]
                                                                 :password [:not-empty :ok-password]}))))
