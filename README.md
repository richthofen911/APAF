# APAF
Android Pubsub Application Framwork

⋅ create a class which extends the third-party pubsub client provider's default callback(if exists) and also implements<br> 
⋅ the GeneralPubsubCallback in this framework<br>
⋅ wrap the third-party pubsub client's default Message class(if exists) with the Message class in this framework<br>
⋅ create a class implementing the PubsubProviderClient interface in this framework<br>
⋅ create a service extending the ServiceMsgIOCenter in this framework<br>
⋅ after service bound:<br>
  1). must call setPubsubProviderClient<br>
  2). must call setSubChannel/setPubChannel before calling subToChannel/pubToChannel<br>
