(ns pingcrm.shared.form-input
  (:require [reagent.core :as r]))

(defn select-input
  [{:keys [label name class errors] :as props} & children]
  (let [opened? (r/atom false)]
    (fn []
      [:div {:class class}
       (when label
         [:label.form-label {:html-for name} label ":"])
       ; (into [:select (merge props {:id name
       ;                              :name name
       ;                              :class (str "form-select mode-lr" (when (seq errors) " error"))})]
       ;       children)
       [:div {:class "relative"}
        [:button
         {:class "cursor-pointer select-none form-select w-11 absolute left-0 bottom-0 left-auto z-20 px-2 text-sm whitespace-nowrap bg-white rounded shadow-xl"
          :on-click #(reset! opened? not)}]
        [:div {:class (str "absolute" (when-not @opened? " hidden"))}
         [:div {:class "fixed inset-0 z-20 bg-black opacity-25"
                :on-click #(reset! opened? false)}]
         [:div {:class "relative z-30 ml-12 py-4 px-6 mr-2 bg-white rounded shadow-lg"}
          [:div {:class "h-full flex flex-col"}
           [:a
            {
             :class "py-6 px-2 hover:bg-indigo-600 hover:text-white",
             :on-click #(swap! opened? not)} "My Profile"]
           [:a
            {
             :class "py-6 px-2 hover:bg-indigo-600 hover:text-white",
             :on-click #(swap! opened? not)} "Manage Users"]]]]]

       (when errors
         [:div.form-error errors])])))

(defn text-input [{:keys [label name class errors] :as props}]
  [:div {:class class}
   (when label
     [:label.form-label {:html-for name} label ":"])
   [:div (merge props {:id name
                       :name name
                       :content-editable "true"
                       :class (str "form-input single-line" (when (seq errors) " error"))})]
   (when errors [:div.form-error errors])])
