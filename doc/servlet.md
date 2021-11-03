# eyeCloudAI - MRMS REST servlet
|URI|Method|Parameter|Body|Return|설명|
|---|------|---------|----|------|---|
|/mrms/get_cvt_fn|GET|-|-| - success : List<Json> Format (String) </br> - error : "error"|변환함수 리스트
|/mrms/project_create|POST|-|{"project_id" : (String), </br> "project_purpose_cd" : (String), </br> "dataset_id" : (String), </br> "status" : (String), </br> "start_time" : (String, YYYYmmddHHMMSS), </br> "modeling_mode" : (String), </br> "project_target_data" : (String)}| - success : "1" </br> - error : "error" |Insert New Project
|/mrms/get_proj_sttus_cd|GET|?project_id=(String)|-|- success : Status(String) </br> - error : "error" |Get project Status Code
|/mrms/get_sttus_cd|POST|-|{"hist_no": (String)}|- success : Status(String) </br> - error : "error" |Get Learning Status Code
