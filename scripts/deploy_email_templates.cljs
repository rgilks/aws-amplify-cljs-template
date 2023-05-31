(ns deploy-email-templates
  (:require ["@aws-sdk/client-ses" :refer [SESClient UpdateTemplateCommand CreateTemplateCommand]]
            ["fs" :as fs]
            ["path" :as path]
            [promesa.core :as p]))

(defn get-template [file-name]
  (.toString
   (fs/readFileSync
    (path/join (path/resolve ".") "emails" file-name))))

(defn deploy-email-template [{:keys [init name subject env]}]
  (p/let [html-template  (get-template (str name ".html"))
          plain-template (get-template (str name ".txt"))
          params {:Template
                  {:TemplateName (str "cljst-" name "-" env)
                   :SubjectPart  subject
                   :TextPart     plain-template
                   :HtmlPart     html-template}}
          client (new SESClient
                      #js {:apiVersion "2010-12-01" :region "eu-west-1"})
          command (new
                   (if init CreateTemplateCommand UpdateTemplateCommand)
                   (clj->js params))
          response (.send client command)
          status   (get-in response ["$metadata" "httpStatusCode"])]
    (when (= status 200)
      (println "DONE"))))

(deploy-email-template
 {:init false :name "generic" :subject "{{title}}" :env "devcljst"})
