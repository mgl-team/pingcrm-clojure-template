(ns pingcrm.shared.icon)

(defn cheveron-down [class]
  [:svg {:class class
         :xmlns "http://www.w3.org/2000/svg"
         :view-box "0 0 20 20"}
   [:path {:d "M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z"}]])

(defn icon [{:keys [name class]}]
  (case name
    "cheveron-down" [cheveron-down class]))
