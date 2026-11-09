# Temoria Discs (NeoForge 1.21.1)

Mod Minecraft NeoForge 1.21.1 ajoutant des disques de musique pour **Temoria** et l'OST complète de **Vintage Story** sans aucune perte de qualité audio.

Le projet est découpé en **3 modules indépendants** pour respecter la limite d'upload de 500 MiB de Modrinth tout en maintenant **100% de la fidélité audio d'origine** :

## Modules & Fichiers JAR

1. **`temoria_disc-1.0.0.jar` (~2.3 Mo)**
   - Mod Core / Base : Enregistre les deux onglets créatifs et le disque exclusif Temoria :
     - **Temoria - Noah t'es ban !**
   - Rareté : Épique (`Rarity.EPIC`).

2. **`temoria_disc_vs_vol1-1.0.0.jar` (~214 Mo)**
   - Vintage Story OST (Volume 1) : Contient 26 musiques (de *Adventuring* à *Nostalgic*).
   - S'ajoute automatiquement dans l'onglet **« Disques - Vintage Story »**.
   - Dépendance requise : `temoria_disc`.

3. **`temoria_disc_vs_vol2-1.0.0.jar` (~215 Mo)**
   - Vintage Story OST (Volume 2) : Contient 26 musiques (de *Peaceful Village* à *Winter*, incluant *Vintage Story*).
   - S'ajoute automatiquement dans l'onglet **« Disques - Vintage Story »**.
   - Dépendance requise : `temoria_disc`.

## Onglets créatifs en jeu

Les onglets créatifs restent exactement les mêmes :
- **Disques - Temoria** : Contient les morceaux exclusifs Temoria.
- **Disques - Vintage Story** : Rassemble les 52 musiques de Vintage Story réparties dynamiquement par le Volume 1 et le Volume 2 via l'événement `BuildCreativeModeTabContentsEvent`.

## Compilation

```powershell
.\gradlew build
```

Les trois fichiers JAR sont générés dans `build/libs/`.
