# TLMonthYearPicker
Simple month and year picker for android.

![TLMonthYearPicker](https://github.com/minhnn2607/TLMonthYearPicker/blob/master/20190115_163411.gif)

## Setup
Add dependency to your __build.gradle__

```groovy		
repositories {
    jcenter()
}
```	
```groovy		
implementation 'vn.nms.mypicker:TLMonthYearPickerView:1.0.0'
```	
## Usage
Add __TLMonthYearPickerView__ to your layout

```groovy	
        app:myp_max_year="2020" //Set max year
        app:myp_min_year="1980" //Set min year
        app:myp_init_year="1990" //Set init year
        app:myp_type="month_year" //type = month/month_year
```

Set your custom listener
```groovy
yearPicker.setListener(new IMonthYearPickerListener() {
            @Override
            public void didSelectDate(Date date) {
                //Your action
            }
          });
```
## Develop By
Minh Nguyen
        
## License
```
Copyright 2017 Minh Nguyen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

          
        
