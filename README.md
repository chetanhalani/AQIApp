# AQIApp
Showing AQI data real time

Chart Library - MPClearAndroidChart

Architecture

Data
Activity will have all the updates
- Fragments will be refering to the object of activity and observing the data update
- Since its LiveData, so update will be with both the fragments
 
View
- View Binding has been used for removing boiler plate code
- RecyclerView has data binding, so update will be with RowViewHolder and binding will do rest of things

We have tried to negotiate with HttpClient for 30 seconds ping interval but server is not handeling that, so i have made one function in MainActivity that  we won't accept data if its before 30 seconds with respect to last udpate. Right now its commented for giving real time check.

In Bottom, added textview in activity to have realtime check that socket is connected or not.
