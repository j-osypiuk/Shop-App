<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Document</title>
    <style>
      * {
        margin: 0;
        font-family: sans-serif;
      }
      h1,
      h2 {
        text-align: center;
      }
      table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
        width: 100%;
      }

      td,
      th {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 8px;
      }

      tr:nth-child(even) {
        background-color: #dddddd;
      }

      li {
        margin-top: 5px;
      }
    </style>
  </head>
  <body>
    <div
      class="container"
      style="width: 70%; margin: 25px auto; background-color: #eee"
    >
      <h1 style="padding: 15px 0">SHOP APP</h1>
      <h2 style="padding: 10px 0 15px 0">Thank You for choosing our store!</h2>
      <h3 style="padding: 10px 10px">These are Your order details:</h3>
      <hr />
      <h4 style="padding: 10px 10px 0 10px">First name: ${firstName}</h4>
      <h4 style="padding: 10px 10px">Last name: ${lastName}</h4>
      <hr />
      <h4 style="padding: 10px 10px">Address:</h4>
      <ul style="list-style-type: none; margin-bottom: 20px; padding-left: 10px">
        <li>Country: ${address.country}</li>
        <li>Region: ${address.region}</li>
        <li>City: ${address.city}</li>
        <li>Street: ${address.street}</li>
        <li>Number: ${address.number}</li>
        <li>Postal-code: ${address.postalCode}</li>
      </ul>
      <hr />
      <table>
        <tr>
          <th>Product</th>
          <th>Amount</th>
          <th>Unit price</th>
          <th>Total Discount</th>
          <th>Total price</th>
        </tr>
        <#list products as product>
            <tr>
              <td>${product.productName}</td>
              <td>${product.amount}</td>
              <td>${product.unitPrice}</td>
              <td>${product.totalDiscount}</td>
              <td>${product.totalPrice}</td>
            </tr>
        </#list>
      </table>
      <hr />
      <h4 style="padding-top: 20px; padding-right: 15px; text-align: right">
        Total discount: <strong>${totalDiscount}</strong>
      </h4>
      <u>
        <h4 style="padding: 15px; text-align: right">
          Total price: <strong>${totalPrice}</strong>
        </h4>
      </u>
    </div>
  </body>
</html>