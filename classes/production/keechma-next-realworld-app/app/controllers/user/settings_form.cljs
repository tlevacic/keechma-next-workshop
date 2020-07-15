(ns app.controllers.user.settings-form
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.next.toolbox.pipeline :refer [pswap! preset!] :refer-macros [pipeline!]]
            [keechma.next.controllers.entitydb :as edb]
            [app.api :as api]
            [promesa.core :as p]
            [keechma.next.controllers.form :as form]
            [app.validators :as v]
            [promesa.core :as p]))

(derive :user/settings-form ::pipelines/controller)

(def pipelines
  {:keechma.form/get-data (pipeline! [value {:keys [deps-state*] :as ctrl}]
                            (:current-user @deps-state*))
   :keechma.form/submit-data (pipeline! [value {:keys [deps-state*] :as ctrl}]
                               (let [jwt (:jwt @deps-state*)
                                     user-data (if (seq (:password value)) value (dissoc value :password))]
                                 (api/user-update {:user user-data :jwt jwt}))
                               (edb/insert-named! ctrl :entitydb :user :user/current value)
                               (ctrl/dispatch ctrl :router :redirect! {:page "profile" :user (:username value)}))})

(defmethod ctrl/prep :user/settings-form [ctrl]
  (pipelines/register ctrl (form/wrap pipelines (v/to-validator {:email [:email :not-empty]
                                                                 :image [:url]}))))
