# AiCalendar
### Backend for calendar and planner with AI
- - -
## Usage
## For watching endpoints you can use this links
### Graphical UI
[http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)
### Classic OpenAPI in json
[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Environment variables
- ### GIGACHAT AI
    For getting these variables, you need go to [Sber studio](https://developers.sber.ru/studio),
    login using Sber ID (it`s important), create new project with GigaChat API, get new access token,
    and decode it from Base 64
    - GIGACHAT_CLIENT_ID
    - GIGACHAT_CLIENT_SECRET
- ### JWT
    - #### Production
        - JWT_SIGNING_KEY - your random UUID key for sign JWTs
    - #### Testing
        Test mode is a special mode in which the application accepts only the JWT key 
        specified in TEST_JWT_KEY
        - TEST_MODE_ENABLED - boolean. enabling test mode
        - TEST_JWT_KEY - Any combination of characters that the application will accept as JWT key
- ### Google
    - GOOGLE_CLOUD_CLIENT_ID - Used for android authentication. It can also be used for 
        authentication via [Google OAuth 2.0 Playground](https://developers.google.com/oauthplayground/)