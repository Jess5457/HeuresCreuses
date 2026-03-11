# ⚡ Heures Creuses — Widget Android

## Comment obtenir l'APK (étape par étape)

### Étape 1 — Crée un compte GitHub (gratuit)
1. Va sur https://github.com
2. Clique sur **Sign up**
3. Crée ton compte (email + mot de passe)

---

### Étape 2 — Crée un nouveau dépôt
1. Une fois connecté, clique sur le **+** en haut à droite → **New repository**
2. Nom du dépôt : `heurescreuses`
3. Coche **Public**
4. **Ne coche pas** "Add a README file"
5. Clique **Create repository**

---

### Étape 3 — Upload les fichiers du projet
1. Sur la page de ton dépôt vide, clique sur **"uploading an existing file"**
2. Glisse-dépose **tout le contenu** du dossier `heurescreuses/` que tu as téléchargé
   ⚠️ Important : le dossier `.github` aussi (il est caché, active "afficher les fichiers cachés")
3. Clique **Commit changes**

---

### Étape 4 — GitHub compile automatiquement l'APK !
1. Va dans l'onglet **Actions** de ton dépôt
2. Tu verras un job "Build APK" qui tourne (⏳ ~3-5 minutes)
3. Une fois terminé (✅ vert), clique dessus
4. En bas de la page, dans **Artifacts**, télécharge **heurescreuses-debug**

---

### Étape 5 — Installe l'APK sur ton Android
1. Transfère le fichier `.apk` sur ton téléphone (Drive, câble, etc.)
2. Sur Android : **Paramètres → Sécurité → Sources inconnues** → Autoriser
3. Ouvre le fichier APK pour installer
4. Lance l'app, configure tes heures creuses
5. **Maintiens le doigt** sur l'écran d'accueil → Widgets → "Heures Creuses" → glisse-le !

---

## Fonctionnalités
- 🌙 Widget qui affiche l'état actuel (creuse / pleine)
- ⏱ Heure actuelle et prochain changement visible dans le widget
- ⚙ App pour configurer tes plages horaires (supporte minuit)
- 🔄 Mise à jour automatique toutes les minutes
