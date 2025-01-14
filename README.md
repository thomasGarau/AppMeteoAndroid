# AppMeteo

**AppMeteo** est une application météo développée en Kotlin avec Jetpack Compose. Elle permet de consulter les prévisions météorologiques pour une localisation actuelle, des villes favorites, ou toute autre ville recherchée. L'application inclut également un système de cache pour un accès hors ligne aux données météo.

---

## Fonctionnalités

### 1. Page principale (Home)
- **Localisation actuelle :** La météo de l'emplacement actuel est affichée en haut de la page, avec des informations comme la température et les conditions météorologiques.
- **Villes favorites :** Liste des villes enregistrées comme favorites. Pour chaque ville, les informations météo sont récupérées et affichées.
- **Actions :** 
  - Supprimer une ville des favoris.

---

### 2. Page de recherche
- **Recherche de villes :** Une barre de recherche permet de rechercher une ville par son nom.
- **Ajout aux favoris :** Les villes trouvées peuvent être ajoutées à la liste des favoris.
- **Consultation météo :** Il est possible de consulter la météo d'une ville sans l'ajouter aux favoris.

---

### 3. Système de cache
- Les données météo des villes favorites sont sauvegardées localement.
- **Mode hors ligne :** Permet de consulter les prévisions météo des villes favorites sans connexion Internet.

---

## Installation et Exécution

### Prérequis
- Android Studio installé.
- Clé API pour la localisation (si nécessaire).

### Instructions
1. Clonez ce dépôt :
   ```bash
   git clone https://github.com/votre-projet.git
### 2. Ouvrez le projet
1. Lancez **Android Studio**.
2. Importez le projet depuis le répertoire où il a été cloné.
3. Assurez-vous d’avoir installé un émulateur ou un appareil physique connecté pour tester l’application.

### 3. Exécutez l'application
1. Cliquez sur le bouton **Run** ou utilisez le raccourci `Shift + F10`.
2. Autorisez l’application à accéder à la localisation si demandé.
3. Redémarrez l'application après avoir autorisé la localisation pour garantir le bon fonctionnement de la fonctionnalité de météo actuelle.

---

## Bugs connus
### 1. Localisation actuelle
Lors du premier lancement, si l'autorisation de localisation est demandée, il est nécessaire de **relancer l'application** après avoir donné l’autorisation pour que la météo de l'emplacement actuel s'affiche correctement.

### 2. Recherche de villes
Certaines recherches provoquent la fermeture de l'application, comme lors de la saisie de `rei` (pour `Reims`) ou `bordea` (pour `Bordeaux`). Cependant, d'autres villes comme `Paris` ou `Nice` fonctionnent sans problème.
