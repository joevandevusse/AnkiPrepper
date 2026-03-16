# AnkiPrepper

Converts CSV flashcard decks exported from [Noji](https://noji.io) into a format ready to import into [Anki](https://apps.ankiweb.net).

## How it works

1. Place Noji CSV exports in the `noji_decks/` folder (filenames include a timestamp, e.g. `us_state_territory_capitals_2026_03_09_004406.csv`).
2. Run the program — it strips the timestamp, prepends a unique ID to each row, and writes the result to `anki_decks/`.
3. Import the output CSV from `anki_decks/` into Anki.

### Example

Input (`noji_decks/us_state_territory_capitals_2026_03_09_004406.csv`):
```
Indiana,Indianapolis
Texas,Austin
Oklahoma,Oklahoma City
```

Output (`anki_decks/us_state_territory_capitals.csv`):
```
US_STATE_TERRITORY_CAPITALS_0001,Indiana,Indianapolis
US_STATE_TERRITORY_CAPITALS_0002,Texas,Austin
US_STATE_TERRITORY_CAPITALS_0003,Oklahoma,Oklahoma City
```

Files that have already been processed (output file exists) are skipped automatically.

## Requirements

- Java 21
- Maven

## Running

Run the `Prepper` main class directly from IntelliJ.
