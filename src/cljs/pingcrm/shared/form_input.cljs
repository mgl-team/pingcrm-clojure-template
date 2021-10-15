(ns pingcrm.shared.form-input
  (:require [reagent.core :as r]
            [reagent.dom :as reagent-dom]
            [applied-science.js-interop :as j]))

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
         {:class "cursor-pointer select-none form-select w-11 absolute left-0 bottom-0 left-auto z-20 px-2 text-sm whitespace-nowrap bg-white rounded"; shadow-xl
          :on-click #(do (reset! opened? not)
                         (.preventDefault %))}]
        [:div {:class (str "absolute" (when-not @opened? " hidden"))}
         [:div {:class "fixed inset-0 z-20 bg-black opacity-25"
                :on-click #(reset! opened? false)}]
         [:div {:class "relative w-48 z-30 ml-12 py-4 px-6 mr-2 bg-white rounded shadow-lg"}
          [:div {:class "h-full w-40 flex flex-col overflow-x-auto"}
           (for [item (first (first children))
                 :let [{:keys [id name]} (j/lookup item)]]
             ^{:key (or id name)}
             [:a {:class "py-6 px-2 hover:bg-indigo-600 hover:text-white",
                  :on-click #(swap! opened? not)} name])]]]]
       (when errors
         [:div.form-error errors])])))

(defn text-input [{:keys [label name class errors value] :as props}]
  (r/create-class
   {:component-did-mount
    (fn [component]
      (let [el (aget (.-children (reagent-dom/dom-node component)) (if label 1 0))]
        (j/assoc! el "innerText" value)))

    :reagent-render
    (fn []
      [:div {:class class}
       (when label
         [:label.form-label {:html-for name} label ":"])
       [:div (merge props {:id name
                           :name name
                           :content-editable "true"
                           :class (str "form-input single-line" (when (seq errors) " error"))})]
                           ; :dangerouslySetInnerHTML {:__html value}})]
       (when errors [:div.form-error errors])])}))
