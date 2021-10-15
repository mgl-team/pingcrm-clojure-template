(ns pingcrm.pages.login
  (:require ["@inertiajs/inertia-react" :refer [useForm Head]]
            [applied-science.js-interop :as j]
            [pingcrm.shared.buttons :refer [loading-button]]
            [pingcrm.shared.form-input :refer [text-input]]
            [pingcrm.shared.logo :refer [logo]]
            [clojure.string :as str]))

(defn login-form []
  (let [{:keys [data setData errors post processing]} (j/lookup
                                                       (useForm #js {:email "mongolian@example.com"
                                                                     :password "secret"
                                                                     :remember false}))
        on-submit #(do (.preventDefault %)
                       (js/console.log (str "---" (.-email data) "---" (.-password data)))
                       (post (js/route "login.store")))]
    [:<>
     [:> Head {:title "Login"}]
     [:div {:class "p-6 bg-indigo-800 min-h-screen flex justify-center items-center"}
      [:div {:class "w-full max-w-md"}
       [logo {:class "block mx-auto w-full max-w-xs fill-white" :height "50"}]
       [:form {:on-submit on-submit
               :class "mt-8 overflow-hidden bg-white rounded-lg shadow-xl mode-lr"}
        [:div {:class "py-10 px-12"}
         [:h1 {:class "text-3xl font-bold text-center"} "Welcome Back!"]
         [:div {:class "h-24 mx-auto mr-6 border-l-2"}]
         [text-input {:class "mr-10"
                      :label "Email"
                      :name "email"
                      :errors (.-email errors)
                      :value (.-email data)
                      :on-input #(setData "email" (.. % -target -innerText))}]
         [text-input {:class "mr-6"
                      :label "Password"
                      :name "password"
                      :type "password"
                      :errors (.-password errors)
                      :value (.-password data)
                      :on-input #(setData "password" (.. % -target -innerText))}]
         [:label {:class "flex items-center mr-6 select-none" :html-for "remember"}
          [:input#remember
           {:name "remember"
            :class "mb-1"
            :type "checkbox"
            :checked (.-remember data)
            :on-change #(setData "remember" (.. % -target -checked))}]
          [:span {:class "text-sm"} "Remember Me"]]]
        [:div {:class "py-10 px-4 bg-gray-100 border-r border-gray-100 flex"}
         [loading-button
          {:type "submit" :loading processing :class "btn-indigo  mt-auto"}
          "Login"]]]]]]))

(defn login []
  [:f> login-form])
