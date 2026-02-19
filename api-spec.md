## 1. login

cmd
```bash
curl 'https://ichigoreader.com/auth/login' \
  -H 'accept: */*' \
  -H 'accept-language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6,fr;q=0.5,hr;q=0.4,lv;q=0.3,lo;q=0.2' \
  -H 'content-type: application/json' \
  -b '_0d8a7=http://15.235.84.252:8080' \
  -H 'origin: https://ichigoreader.com' \
  -H 'priority: u=1, i' \
  -H 'referer: https://ichigoreader.com/login?redirect=/upload' \
  -H 'sec-ch-ua: "Not:A-Brand";v="99", "Google Chrome";v="145", "Chromium";v="145"' \
  -H 'sec-ch-ua-mobile: ?1' \
  -H 'sec-ch-ua-platform: "Android"' \
  -H 'sec-fetch-dest: empty' \
  -H 'sec-fetch-mode: cors' \
  -H 'sec-fetch-site: same-origin' \
  -H 'user-agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Mobile Safari/537.36' \
```
--data-raw '{"email":"blackfeather227@gmail.com","password":"password"}'

response
```json
{
    "user": {
        "email": "blackfeather227@gmail.com",
        "dateCreated": "2025-07-29T17:04:38",
        "emailStatus": "unverified"
    },
    "tokens": {
        "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJibGFja2ZlYXRoZXIyMjdAZ21haWwuY29tIiwiaWF0IjoxNzcxNDg5ODY2LCJuYmYiOjE3NzE0ODk4NjYsImp0aSI6IjU2OGIxZDMzLTkwYjgtNDQwOS04OGUxLWFkNzM3MzZiMzFlYyIsInR5cGUiOiJhY2Nlc3MiLCJmcmVzaCI6ZmFsc2V9.zLVjiekb0_FuDiMBq71Yuf3aLEEqVUOh0TVXC6sHHvk",
        "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJibGFja2ZlYXRoZXIyMjdAZ21haWwuY29tIiwiaWF0IjoxNzcxNDg5ODY2LCJuYmYiOjE3NzE0ODk4NjYsImp0aSI6ImVjYmM4YmIyLTNiYWYtNDMwMC1iNjFlLThmZjc5Y2I1NGJmYiIsInR5cGUiOiJyZWZyZXNoIn0.YTvfSfD4Rq1h3Z76cg1x6FxZMKqPZ7q-27BN6lHtRWo"
    },
    "subscription": {}
}
```

## 2. upload

cmd
```bash
curl 'https://ichigoreader.com/translate/as-reader-format' \
  -H 'accept: */*' \
  -H 'accept-language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6,fr;q=0.5,hr;q=0.4,lv;q=0.3,lo;q=0.2' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundaryM8l89Fqg9oTKryUr' \
  -b '_0d8a7=http://15.235.84.252:8080; access_cookie=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJibGFja2ZlYXRoZXIyMjdAZ21haWwuY29tIiwiaWF0IjoxNzcxNDg4MjcxLCJuYmYiOjE3NzE0ODgyNzEsImp0aSI6ImQyODg2MGMwLTRjNWYtNDA4Yi05ZDAwLWNhMmM1NDkzYmJhMiIsInR5cGUiOiJhY2Nlc3MiLCJmcmVzaCI6ZmFsc2V9.vWNRQwJfEKXnDlVa4va0PAFpa0wvuT2lhTo8BbrUfU0; refresh_token_cookie=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJibGFja2ZlYXRoZXIyMjdAZ21haWwuY29tIiwiaWF0IjoxNzcxNDg4MjcxLCJuYmYiOjE3NzE0ODgyNzEsImp0aSI6ImUxOWUyYTlhLTBmYjktNGU3MS05MDQ0LWU4YTgzNGFiMTJhNiIsInR5cGUiOiJyZWZyZXNoIn0.enqCOdSnE-679oWoGPlmxDDDp9pxX9lFFfJWAenqYoQ' \
  -H 'origin: https://ichigoreader.com' \
  -H 'priority: u=1, i' \
  -H 'referer: https://ichigoreader.com/upload' \
  -H 'sec-ch-ua: "Not:A-Brand";v="99", "Google Chrome";v="145", "Chromium";v="145"' \
  -H 'sec-ch-ua-mobile: ?1' \
  -H 'sec-ch-ua-platform: "Android"' \
  -H 'sec-fetch-dest: empty' \
  -H 'sec-fetch-mode: cors' \
  -H 'sec-fetch-site: same-origin' \
  -H 'user-agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Mobile Safari/537.36' \
  --data-raw $'------WebKitFormBoundaryM8l89Fqg9oTKryUr\r\nContent-Disposition: form-data; name="file"; filename="Downloads.zip"\r\nContent-Type: application/x-zip-compressed\r\n\r\n\r\n------WebKitFormBoundaryM8l89Fqg9oTKryUr\r\nContent-Disposition: form-data; name="fingerprint"\r\n\r\n5178b9c834da2c1d9aa539bebc022cdf23a181d1\r\n------WebKitFormBoundaryM8l89Fqg9oTKryUr\r\nContent-Disposition: form-data; name="targetLangCode"\r\n\r\nko\r\n------WebKitFormBoundaryM8l89Fqg9oTKryUr\r\nContent-Disposition: form-data; name="translationModel"\r\n\r\nundefined\r\n------WebKitFormBoundaryM8l89Fqg9oTKryUr--\r\n'
```

response
```json
{
    "userId": "blackfeather227@gmail.com",
    "jobId": "cdf968ef-0679-4efc-adda-5b5fd0df51fa",
    "totalImageCount": 2,
    "translatedImageCount": 0,
    "contextPageCount": 0,
    "status": "building-context",
    "subscriptionTier": "tier-1",
    "targetLangCode": "ko",
    "translationModel": "gemini-flash",
    "advancedOcrDisabled": true,
    "fileName": "Downloads.zip",
    "contentType": "application/x-zip-compressed",
    "internal": null,
    "fileLocation": null,
    "imageLimit": 2,
    "createdAt": "2026-02-19T08:05:08.840431+00:00"
}
```

## 3. status check 

cmd

```bash
curl 'https://ichigoreader.com/translate/as-reader-format/get?jobId=cdf968ef-0679-4efc-adda-5b5fd0df51fa' \
  -H 'accept: */*' \
  -H 'accept-language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6,fr;q=0.5,hr;q=0.4,lv;q=0.3,lo;q=0.2' \
  -b '_0d8a7=http://15.235.84.252:8080; access_cookie=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJibGFja2ZlYXRoZXIyMjdAZ21haWwuY29tIiwiaWF0IjoxNzcxNDg4MjcxLCJuYmYiOjE3NzE0ODgyNzEsImp0aSI6ImQyODg2MGMwLTRjNWYtNDA4Yi05ZDAwLWNhMmM1NDkzYmJhMiIsInR5cGUiOiJhY2Nlc3MiLCJmcmVzaCI6ZmFsc2V9.vWNRQwJfEKXnDlVa4va0PAFpa0wvuT2lhTo8BbrUfU0; refresh_token_cookie=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJibGFja2ZlYXRoZXIyMjdAZ21haWwuY29tIiwiaWF0IjoxNzcxNDg4MjcxLCJuYmYiOjE3NzE0ODgyNzEsImp0aSI6ImUxOWUyYTlhLTBmYjktNGU3MS05MDQ0LWU4YTgzNGFiMTJhNiIsInR5cGUiOiJyZWZyZXNoIn0.enqCOdSnE-679oWoGPlmxDDDp9pxX9lFFfJWAenqYoQ' \
  -H 'priority: u=1, i' \
  -H 'referer: https://ichigoreader.com/upload' \
  -H 'sec-ch-ua: "Not:A-Brand";v="99", "Google Chrome";v="145", "Chromium";v="145"' \
  -H 'sec-ch-ua-mobile: ?1' \
  -H 'sec-ch-ua-platform: "Android"' \
  -H 'sec-fetch-dest: empty' \
  -H 'sec-fetch-mode: cors' \
  -H 'sec-fetch-site: same-origin' \
  -H 'user-agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Mobile Safari/537.36'
```

response(ongoing)

```json
{
    "userId": "blackfeather227@gmail.com",
    "jobId": "cdf968ef-0679-4efc-adda-5b5fd0df51fa",
    "totalImageCount": 2,
    "translatedImageCount": 0,
    "contextPageCount": 2,
    "status": "translating",
    "subscriptionTier": "tier-1",
    "targetLangCode": "ko",
    "translationModel": "gemini-flash",
    "fileName": "Downloads.zip",
    "contentType": "application/x-zip-compressed",
    "internal": null,
    "fileLocation": null,
    "imageLimit": 2,
    "createdAt": "2026-02-19T08:05:08.840431+00:00"
}
```

response(finished)

```json

```

## 4. download

url: https://ichigoreader.com/translate/as-reader-format/download?jobId=cdf968ef-0679-4efc-adda-5b5fd0df51fa