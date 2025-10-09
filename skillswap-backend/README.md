3️⃣ Generate Your Own JWT Secret

Each developer must generate a unique 32-byte Base64 key.

 macOS / Linux  
openssl rand -base64 32  

 Windows PowerShell  
[Convert]::ToBase64String((1..32 | ForEach-Object {Get-Random -Minimum 0 -Maximum 256}))

Example output  
jVRSDQB9wrRIAMDXbmnkbSiEAFvjxmpWy6ZQojeZLes=

Paste that into your new local file:

jwt.secret=jVRSDQB9wrRIAMDXbmnkbSiEAFvjxmpWy6ZQojeZLes=

⚠️ Important:
Never commit your real `application-local.properties` file.  
Git already ignores it automatically.  
Only `application-local.properties.example` should be versioned.

---

4️⃣ Run the Backend

To start the API server:
```bash
mvn spring-boot:run

