# samta-ai-task
**There is swagger URL** `http://localhost:6060/e-commerce/swagger-ui.html`
**Base URL** - `localhost:6060/e-commerce`

**Authentication mechanism**
- Admin default email `shiv@gmail.com` password `shiv`
- Login **POST** - `http://localhost:6060/e-commerce/authentication/login` and in body you have to specify user type 1 for Admin and 2 for Customers then generate Access and Refresh token both
- Access token has 6 hrs expiry time while refresh token has expiry time 7 days.
- You can use access token with Bearer text.
- After access token expired then you can generate new access and refresh token both via existing refresh token.
- Generate refresh token **POST** - `http://localhost:6060/e-commerce/authentication/refresh`

**Customer API's**

- Add customer with request method **POST** - `http://localhost:6060/e-commerce/customer`.
- Update customer with request method **PUT** - `http://localhost:6060/e-commerce/customer/{id}`.
- Get customer with request method **GET** - `http://localhost:6060/e-commerce/customer/{id}`.
- Delete customer with request method **DELETE** - `http://localhost:6060/e-commerce/customer/{id}`.
- Getting product with in store with request method **GET** - `http://localhost:6060/e-commerce/customer/store-product/{storeIid}/{productId}`.
- Getting products with in store with request method **GET** - `http://localhost:6060/e-commerce/customer/store-products/{storeIid}`.
- Getting products with in stores with request method **GET** - `http://localhost:6060/e-commerce/customer/stores-products`.

**Product APIs**

- Save all products into the store via **.xlsx file only not .csv** with request method **POST** - `http://localhost:6060/e-commerce/product/bulk/{storeId}/{file}`.
- For test data [Products.xlsx](https://github.com/DevShivmohan/samta-ai-task/files/11712429/Products.xlsx)


**Vendors APIs**

- Add vendor **POST** - `http://localhost:6060/e-commerce/vendor`.
- update vendor **PUT** - `http://localhost:6060/e-commerce/vendor/{id}`.
- delete vendor **DELETE** - `http://localhost:6060/e-commerce/vendor/{id}`.
- Get vendor **GET** - `http://localhost:6060/e-commerce/vendor/{id}`.
- Get all vendor **GET** - `http://localhost:6060/e-commerce/vendor`.

- 
