# CircularTimePickerView

A custom circular time picker for Android, allowing users to select hours and minutes with smooth touch interactions and step-based snapping.

---

## Screenshots
<img width="400" height="519" alt="Screenshot_20250820_160116" src="https://github.com/user-attachments/assets/63c8bdba-13b9-439c-b90d-29467bf13e26" />
<img width="400" height="519" alt="Screenshot_20250820_1601162" src="https://github.com/user-attachments/assets/0760ac82-a1c9-4b67-adbd-c67b9f80c1c1" />


## Features

- **Hour selection** (12-hour or 24-hour modes)  
- **Minute selection** with configurable step increments  
- **Snap-to-step** feature (e.g., every 30 degrees for hours or every 6 minutes for minutes)  
- Circular design with **inner and outer rings**  
- Smooth **touch and drag** interactions  
- Customizable **colors and line thickness**  

---

## Requirements
- Add it in your root settings.gradle at the end of repositories:
```gradle
maven { url 'https://jitpack.io' }
```
---

## Installation
```gradle
implementation 'androidx.appcompat:appcompat:1.3.0'
implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
```
---

## How to use

- Include the CircularTimePickerView class in your XML.

```XML Usage
<com.mehrdad.circulartimepicker.CircularTimePickerView
            android:id="@+id/timePicker"
            android:layout_width="300dp"
            android:layout_height="300dp"/>
```

- In your Activity

```Activity

val timePicker = this.findViewById(R.id.timePicker)

```

- Callback
```Callback
timePicker.onTimeSelected = { hour, minute ->
            textViewHour.text = String.format(Locale.US, "%02d", hour)
            textViewMinute.text = String.format(Locale.US, "%02d", minute)
        }
```

- Select hour again after choosing minute
```reset
timePicker.resetToHourMode()
```

- Selected hour
```hour
val hour:Int = timePicker.getSelectedHour()
```

- Selected minute
```minute
val minute:Int = timePicker.getSelectedMinute()
```

---

## Customization

- Set default time

```default
timePicker.setSelectedTime(hour = 8, minute = 30)
```  
- Set style
```style
timePicker.setStrokeStyle(
            strokeColor = context.getColor(R.color.color1),
            numberColor = context.getColor(R.color.color2),
            circle = context.getColor(R.color.primary100),
            width = 20.0f,
            handlerWidth = 3f)
```
---
