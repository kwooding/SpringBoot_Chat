# SpringBoot_Chat

## WebSocket Integration
### Initially this project started with the integration of a websocket without using any predefined STOMP tools from the Spring ecosystem. This meant adding users to channels, getting messages and Sending messages where all done distinctly with loops checking each users validity and sending the message over the socket through JSON, as opposed to using standard text.

### As this project evolved it then implemented the STOMP framework for websocket configuration where data would now be pushed to endpoints where data could be sent and received by each user and both users could see the message in a JSON format with the users name, message and the timestamp of when it was sent.

## WebSocket Authentication
### Authentication was done through JWTs(JSON Web Tokens) where each user would be assigned a token on login. This was used to authenticate users and allow for more security in communication across users

## Redis Implementation
### The first task that I undertook with Redis was the usage of its pub/sub capabilties to be able to create 