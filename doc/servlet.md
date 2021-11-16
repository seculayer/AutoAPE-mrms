# eyeCloudAI - MRMS REST servlet
|URI|Method|Parameter|Body|Return|Notes|
|---|------|---------|----|------|-----|
|/mrms/get_cvt_fn|GET|-|-| - success : List<Json> Format (String) </br> - error : "error"|변환함수 리스트|
|/mrms/project_create|POST|-|{"project_id" : (String), </br> "project_purpose_cd" : (String), </br> "dataset_id" : (String), </br> "status" : (String), </br> "start_time" : (String, YYYYmmddHHMMSS), </br> "modeling_mode" : (String), </br> "project_target_data" : (String)}| - success : "1" </br> - error : "error" |Insert New Project|
|/mrms/get_proj_sttus_cd|GET|?project_id=(String)|-|- success : Status(String) </br> - error : "error" |Get project Status Code|
|/mrms/get_sttus_cd|POST|-|{"hist_no": (String)}|- success : Status(String) </br> - error : "error" |Get Learning Status Code|
|/mrms/status_update/learn|POST|-|{"hist_no": (String), </br> "task_idx": (String),  </br> "learn_sttus_cd": (String),  </br> "message": (String)}|- success : "1" </br> - error : "error" |Update Learning Status Code|
|/mrms/eps_update|POST|-|{"eps"" : (String), </br> "hist_no" : (String)}|- success : "1" </br> - error : "error" |Update EPS(Events Per Second)|
|/mrms/learn_result_update|POST|-|{"result": (JSON Format String), </br> "hist_no": (String)}|- success : "1" </br> - error : "error" |Update Learn Result|
|/mrms/results/learn|POST|-|{"hist_no": (String) </br> "task_idx": (String), </br> "global_sn": (String), </br> "rst_type": (String),</br> "result: (List)}|- success : "1" </br> - error : "error" |get Learn Results|
|/mrms/get_learn_logs|GET|?id={learn_hist_no}&tail={true or false}|-| (String) JSON logs </br> ex){"worker_{idx}": "log string"}| get Learning Pod Logs(ALL workers)

