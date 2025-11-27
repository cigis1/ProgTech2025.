# ProgTech2025.
# Amőba Játék

Készítette: Soós Roland (TSEQ51)

## Játék Alapok

A játék két játékos között zajlik: az egyik a felhasználó (User), a másik pedig az AI (Mesterséges Intelligencia).

### Játékmenet


**Kezdés:** A játékot a felhasználó indítja, és lerak egy piros **X**-et.

**AI lépése:** Ezt követően az AI véletlenszerű helyre elhelyez egy kék **O**-t.

**Játékmenet:** A felhasználó ismét lerak egy X-et, majd a lépések felváltva folytatódnak egészen a győzelemig.

**Eredmények mentése:** A győzelmeket a rendszer adatbázisban tárolja, és a játék végén megjeleníti a legjobb játékosokat (felhasználónév és győzelmek száma).

---

## **Játékszabályok**

**AI védekezése:** Ha a felhasználónak két összefüggő X-e van egy vonalban, az AI felismeri a veszélyt, és blokkolja a harmadik X helyét, hogy megakadályozza a sor kialakulását.

**Győzelem feltétele:** A játék addig tart, amíg valamelyik játékosnak sikerül 5 jelet megszakítás nélkül egy vonalba rendeznie. Aki először eléri ezt, az nyer.




