Coin Machine

Available endpoints:

- GET::/api/machine/total-coins

- GET::/api/machine/transaction/all

- POST::/api/machine/change-bills

Json body sample:
```
[10, 5, 100]
```
- POST::/api/machine/add-coins

Json body sample:
```
[
    {
        "amount": 25,
        "quantity": 100
    },
    {
        "amount": 10,
        "quantity": 100
    }
]
```

How to run

- Clone the project. 
- Add to your IDE and run CoinMachineApplication.
- You can also run the Integration test or Junit test 

You can also import the Postman file to have some sample to test the application.

