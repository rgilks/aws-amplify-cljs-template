(ns app.config
  (:require
   ["aws-exports" :default aws-exports]))

(def debug?
  ^boolean goog.DEBUG)

(def s3-images-path
  (str "https://"
       (.-aws_user_files_s3_bucket aws-exports)
       ".s3-"
       (.-aws_user_files_s3_bucket_region aws-exports)
       ".amazonaws.com/public/"))
