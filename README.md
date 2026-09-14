# Social Store Inventory Manager

An Android app for a food bank / social store ("Loja Social"), built with a teammate: staff log in, manage a shared inventory of donated goods, register incoming donations and outgoing deliveries to beneficiaries, and pull reports on all three.

## What it does

- **Accounts** — employee login via Firebase Auth, profile & password management
- **Beneficiaries** — register, edit and look up the people the store serves
- **Inventory** — track product stock levels as donations come in and deliveries go out
- **Donations & deliveries** — log each one, with history and near/late-delivery alerts
- **Reports** — dedicated report screens for inventory, donations and deliveries
- **Alerts** — a dedicated screen surfacing low stock and delivery timing issues

## Architecture

MVVM throughout: each feature has a `Screen` (Compose UI), a `ViewModel`, and a `Repository` backed by Firestore (`AuthRepository`, `BeneficiaryRepository`, `ProductRepository`, `DonationRepository`, `DeliveryRepository`, `AlertRepository`). Hilt wires the repositories into the ViewModels; navigation runs through a single `AppNavigation` graph with a shared bottom bar.

## Tech stack

Kotlin · Jetpack Compose · Firebase (Auth, Firestore) · Hilt

## Running

Open the project in Android Studio and run the `app` module. Requires a Firebase project with Auth and Firestore enabled.

## Authors

Guilherme Azeredo & FreverZ — [GitHub](https://github.com/azeredo-99) · [LinkedIn](https://www.linkedin.com/in/guilherme-azeredo-a11bb0254/)
