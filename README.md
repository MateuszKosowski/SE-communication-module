# Instrukcja Integracji z Modułem Komunikacji (Gateway)

Nasz system wykorzystuje architekturę mikroserwisów z centralnym **API Gateway** (Moduł Komunikacji). Poniżej znajdują się zasady, jak Wasze serwisy mają się ze sobą komunikować.

## 1. Główna Zasada
**ZABRONIONE:** Bezpośrednia komunikacja między serwisami (np. Serwis A strzela bezpośrednio na `localhost:8082`).

**WYMAGANE:** Cały ruch musi przechodzić przez Gateway (`localhost:3000`).

## 2. Adresy i Porty
*   **Gateway (Communication Module):** `http://localhost:3000`
*   Wasze serwisy działają na swoich dedykowanych portach (np. 3001, 3002), ale dla "świata zewnętrznego" i innych serwisów jesteście widoczni tylko przez port 3000.

## 3. Jak wołać inny serwis?

Jeśli Twój serwis chce pobrać dane z innego serwisu, musisz wysłać żądanie na adres Gatewaya z odpowiednim **prefiksem**.

**Format URL:**
`http://localhost:3000` + `/api/{nazwa-serwisu}` + `/jakis-endpoint`

### Przykłady Routing'u:

| Kto | Kogo woła | Prawdziwy adres docelowy (ukryty) | URL, na który strzelasz (przez Gateway) |
| :--- | :--- |:----------------------------------| :--- |
| **Service A** | **Service B** | `localhost:3002/inventory/items`  | `http://localhost:3000/api/b/inventory/items` |
| **Frontend** | **Service A** | `localhost:3001/orders/create`    | `http://localhost:3000/api/a/orders/create` |

## 4. Ważne: Prefiksy są wirtualne!

Gateway stosuje mechanizm **StripPrefix**. Oznacza to, że prefiks (np. `/api/b`) służy tylko do skierowania ruchu i jest **usuwany** zanim żądanie trafi do Twojego kontrolera.

**Przykład:**
1. Żądanie wchodzi na Gateway: `GET /api/b/users/1`
2. Gateway usuwa `/api/b`.
3. Do Twojego serwisu trafia czyste: `GET /users/1`

4. **Wniosek dla Developerów:** W swoich kontrolerach (`@RestController`) **NIE** dodawajcie prefiksów `/api/a` czy `/api/b`. Definiujcie swoje ścieżki normalnie, np. `@RequestMapping("/orders")`.

## 5. Jak dodać nowy moduł do systemu?

Jeśli tworzysz nowy serwis (np. `Service C`), musisz zgłosić to do zespołu odpowiedzialnego za Moduł Komunikacji, aby dodać wpis w `application.yml` na Gatewayu.

Wymagane informacje:
1.  **Nazwa serwisu** (ID trasy).
2.  **Port**, na którym Twój serwis wstaje lokalnie.
3.  **Prefiks**, pod którym ma być dostępny (np. `/api/c/**`).
