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

## CSV column formats

### Standard (3 columns)
`ID, Front, Back` — the default format for most decks.

### Image deck (4 columns)
`ID, Front, Image, Back` — used by decks built around Anki's **Trivia Master** note type, where an image is shown on the front alongside the text header. The Image column holds a bare `<img src="filename.ext">` tag referencing a file in `images/<deck_name>/`. Front holds the deck-name header; Back is the clean answer.

Examples: `paintings.csv`, `canadian_provinces_and_territories.csv`

### Back Image deck (5 columns)
`ID, Front, Image, Back, BackImage` — extends the image format with a second image shown on the back. Front Image is typically left empty when the question is text-only; Back Image shows a map or visual answer aid.

Examples: `country_borders.csv`

## Copying images to Anki

After adding new image decks, copy all images to Anki's media folder:

```sh
cp /Users/apple/Documents/Development/Anki/AnkiPrepper/images/*/* \
   "$HOME/Library/Application Support/Anki2/Joe Vande Vusse/collection.media/"
```

## Requirements

- Java 21
- Maven

## Running

Run the `Prepper` main class directly from IntelliJ.
