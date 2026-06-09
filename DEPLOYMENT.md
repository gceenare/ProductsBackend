Render deployment steps for ProductsBackend

1) Create a new Render PostgreSQL database (Managed Service)
   - Go to Render dashboard → New → PostgreSQL
   - Choose name: `products-backend-db` (or any name you prefer)
   - Choose plan (starter for dev is fine)
   - Create DB and copy the connection string when ready (RENDER will show something like `postgres://user:pass@host:5432/dbname`)

2) Create a new Web Service on Render
   - New → Web Service
   - Connect your Git repo (or select manual deploy option)
   - Build Command: `mvn -DskipTests package`
   - Start Command: `java -jar target/ProductsBackend-1.0-SNAPSHOT.jar`

3) Set Environment Variables for the service
   - `SPRING_DATASOURCE_URL` = `jdbc:postgresql://<HOST>:5432/<DBNAME>` (use the Render DB connection details)
     - If Render gave a `postgres://user:pass@host:5432/dbname` URL, convert it to JDBC form:
       `jdbc:postgresql://host:5432/dbname`
   - `SPRING_DATASOURCE_USERNAME` = `<DB_USER>`
   - `SPRING_DATASOURCE_PASSWORD` = `<DB_PASSWORD>`
   - `HIBERNATE_DIALECT` = `org.hibernate.dialect.PostgreSQLDialect`
   - `ADMIN_PASSWORD` = `<YOUR_SECURE_ADMIN_PASSWORD>`

4) (Optional but recommended) Configure health check
   - Health check path: `/actuator/health` (if you enable Spring Actuator) or `/` for simple check
   - Start Timeout: 120s

5) Deploy and check logs
   - Trigger deploy from Render (manual or via git push)
   - Watch logs in Render dashboard for migration messages and successful start

7) Notes
   - The project is configured to accept `SPRING_DATASOURCE_*` env vars; if they are not set it will fall back to the (now updated) default values in `application.properties`.
   - For production, use secure generated database credentials and do not commit secrets to source control.
