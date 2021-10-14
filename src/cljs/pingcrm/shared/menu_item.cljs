(ns pingcrm.shared.menu-item
  (:require ["@inertiajs/inertia-react" :refer [InertiaLink]]
            [pingcrm.shared.icon :refer [icon]]
            [clojure.string :as str]))

(defn menu-item [{:keys [text link icon-name current-url]}]
  (let [active? (or (and (= current-url "") (= link "dashboard")) (str/starts-with? current-url link))
        icon-class (str "h-4 w-4 mb-2 " (if active? "text-white fill-current" "text-indigo-400 group-hover:text-white fill-current"))
        text-class (if active? "text-white" "text-indigo-300 group-hover:text-white")]
    [:div {:class "mr-4 origin-left transform -rotate-45"}
     [:> InertiaLink {:href (js/route link) :class "flex items-center group px-3"}
      [icon {:name icon-name :class icon-class}]
      [:div {:class text-class}
       text]]]))
