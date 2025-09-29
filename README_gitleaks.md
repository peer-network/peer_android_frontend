# 🔒 Gitleaks Pre-Commit Hook (Android Frontend)

This repository uses **[Gitleaks](https://github.com/gitleaks/gitleaks)** to prevent secrets (API keys, passwords, tokens, etc.) from being committed.

---

## 🚀 Setup

Run the setup script once:

```bash
chmod +x setup-hooks.sh
./setup-hooks.sh
```

This will:

- Configure Git to use .githooks/ as the hooks directory.
- Make .githooks/pre-commit executable.
- Ensure gitleaks is installed (v8.28.0).

If missing, the script will download the correct binary for your OS/architecture.

Confirm the hook is ready.

🛡️ Pre-Commit Scan
On every git commit, the hook will:

- Run a Gitleaks scan on staged changes only.
- Block the commit if potential secrets are detected.
- Write results to .gitleaks_out/gitleaks-precommit.json.

If a commit is blocked:

- Check .gitleaks_out/gitleaks-precommit.json for details.
- Remove or mask the secret before retrying.

Do not bypass with git commit --no-verify — CI will still block your PR.

---

###

Docker Fallback

If a local Gitleaks binary is missing, the pre-commit hook will fall back to Docker:

docker run --rm -i -v "$(pwd)":/repo ghcr.io/gitleaks/gitleaks:v8.28.0 detect ...

---

###
✅ Verifying Installation
To check that everything is set up correctly:

```
gitleaks version
```
# should print: 8.28.0

---

###
Local ignores

Create or update a .gitleaksignore file in your repo root with this fingerprints as file content.

Author: \$DISCORD_MENTION
:android-catch-all:251

This file should not be committed — it is listed in .gitignore.

It only affects your local commits.

CI ignores

The same fingerprints are generated automatically in CI before the scan runs.
That way, local and remote scans behave the same.

If you believe a new ignore should be permanent:

Talk to your Team Lead / CTO / DevOps.

They will update either .gitleaksignore in CI or adjust gitleaks.toml to allow it globally.

---

###
🧹 Ignore False Positives
If Gitleaks flags something that is not a real secret:

Talk to your Team Lead / DevOps.

They can add an exception to gitleaks.toml.

With this setup, secrets are scanned locally before every commit and again in CI, ensuring strong security across the repo. 🔐

Gitleaks is set 🚀 