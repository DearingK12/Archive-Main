# D&D Ultimate Dice Roller

## Overview
The **D&D Ultimate Dice Roller** is a browser-based tool for managing Dungeons & Dragons characters and rolling dice. It combines a full-featured character sheet, dice roller, import/export features, and local storage to save your characters across sessions. 

The tool supports:
- Dice rolling with Normal, Advantage, and Disadvantage options
- Animated dice roll display
- Full character sheet (11 sections)
- Save, Load, Delete, Edit workflow
- Import from PDF, CSV, TXT, and image files
- Portrait uploads for characters
- Persistent local storage for saving characters
- Collapsible character sheet for cleaner UI

---

## Features

### Dice Roller
- Select dice type (d4, d6, d8, d10, d12, d20, d100)
- Normal / Advantage / Disadvantage options
- Animated roll effect shows dice rolling before the final result
- Results logged in history

### Character Sheet Sections (Collapsible)
1. **Basic Info**: Name, Campaign, Race, Class, Background, Alignment, Size, Speed  
2. **Ability Scores**: STR, DEX, CON, INT, WIS, CHA, Proficiency Bonus  
3. **Combat Stats**: HP, AC, Initiative, Spell Save DC, Spell Attack  
4. **Skills**: Arcana, History, Investigation, Medicine, Insight  
5. **Tool Proficiencies**: Alchemist’s Supplies, Tinker’s Tools, Thieves’ Tools, Smith’s Tools  
6. **Class Features**: Magical Tinkering, Infuse Item, Right Tool for the Job, Experimental Elixir, Alchemical Savant, Tool Expertise  
7. **Active Infusions**: Enhanced Arcane Focus, Alchemy Jug  
8. **Spellcasting**: Spell Slots, Cantrips, Prepared Spells  
9. **Equipment**: Up to 5 items  
10. **Personality**: Ideal, Bond, Flaw  
11. **Portrait / Import**: Upload portrait image, import character sheet from PDF, CSV, TXT, or image

---

### Save / Load / Edit Workflow
- **Save**: Saves the current character to local storage using character name (and optional campaign)  
- **Load**: Choose a saved character to load into the sheet  
- **Delete**: Delete a saved character  
- **Edit**: Unlock fields to modify the loaded character  
- **Done**: Save edits to overwrite the character  
- **Cancel**: Revert any changes made while editing

---

### Importing Characters
- Supports **PDF, CSV, TXT, and images**
- Uses OCR (via Tesseract.js) for images to extract stats automatically
- Automatically fills the character sheet fields based on recognized text
- Portraits can also be uploaded and displayed

---

### Requirements
- Modern web browser (Chrome, Firefox, Edge, Safari)
- No server required — fully client-side
- Optional: internet connection for Tesseract.js and PDF.js libraries (CDN used)

---

### How to Use
1. Open `index.html` in a web browser.
2. Collapse or expand the **Character Sheet** section as needed.
3. Fill in character info manually or import from a file.
4. Upload a portrait if desired.
5. Roll dice using the dice roller section.
6. Use **Save / Load / Delete / Edit** buttons to manage your characters.
7. Your characters will persist across browser sessions via local storage.

---

### Notes
- Normal dice rolls show a single value; Advantage/Disadvantage rolls show both rolls and the chosen result.
- Local storage is browser-specific — saved characters won’t sync between devices.
- Imported files must be in standard formats for proper parsing.
- Editable fields are locked when you load a character until you press **Edit**.

---

### Credits
- Dice animation inspired by common D&D online tools
- OCR integration via [Tesseract.js](https://github.com/naptha/tesseract.js)
- PDF parsing via [PDF.js](https://mozilla.github.io/pdf.js/)

---

### License
This project is free to use and modify. Attribution appreciated but not required.
