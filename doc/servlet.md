# eyeCloudAI - MRMS REST servlet
|URI|Method|Body|Return|설명|
|---|------|----|------|---|
|/mrms/get_cvt_fn|GET|-| success : List<Json> Format (String)|변환함수 리스트
|/mrms/project_create|POST|{"project_id" : (String), "project_purpose_cd" : (String), "dataset_id" : (String), "status" : (String), "start_time" : (String, YYYYmmddHHMMSS), "modeling_mode" : (String), "project_target_data" : (String)}| success : "1"|Insert New Project
