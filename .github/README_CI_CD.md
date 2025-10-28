# CI/CD Setup Guide for SMNotes

This project uses GitHub Actions to automatically build and deploy APK files to Telegram.

## 🚀 How to Integrate CI/CD into Your Project

### Step 1: Create the Workflow File

Create `.github/workflows/deploy.yml` in your project root with the workflow configuration.

### Step 2: Set Up Telegram Bot

1. Open Telegram and search for `@BotFather`
2. Send `/newbot` and follow instructions to create your bot
3. **Save the bot token** (looks like: `123456789:ABCdefGHIjklMNOpqrsTUVwxyz`)

### Step 3: Get Telegram Chat ID

**For Private Channels/Groups**, use this method:

1. Add your bot to your private channel/group
2. Get your bot token from Step 2
3. Open this URL in your browser:
   ```
   https://api.telegram.org/bot<YOUR_BOT_TOKEN>/getUpdates
   ```
   
   Replace `<YOUR_BOT_TOKEN>` with your actual bot token

4. In the response, look for `"chat":{"id":-123456789}`
5. Copy that number (the chat ID is usually negative for groups: `-1001234567890`)

**For Public Channels**, you can use the channel username directly: `@yourchannelname`

### Step 4: Configure GitHub Secrets

1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**

Add these two secrets:

#### Secret 1: `TELEGRAM_BOT_TOKEN`
- **Name**: `TELEGRAM_BOT_TOKEN`
- **Value**: Your bot token from @BotFather

#### Secret 2: `TELEGRAM_CHAT_ID`
- **Name**: `TELEGRAM_CHAT_ID`
- **Value**: The chat ID you found (e.g., `-1001234567890`) or channel username (`@yourchannelname`)

### Step 5: Deploy

Create and push a version tag:

```bash
git tag v1.0
git push origin v1.0
```

Or manually trigger from the **Actions** tab in GitHub.

## ✅ That's It!

Your CI/CD is now set up. Every time you push a version tag, the APK will be:
- Built automatically
- Uploaded to GitHub Artifacts
- Sent to your Telegram channel
