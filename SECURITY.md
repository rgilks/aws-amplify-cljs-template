# Security Policy

## Supported Versions

This repository is a starter template. Security fixes are applied to the `main`
branch rather than backported to older template snapshots.

## Reporting a Vulnerability

Please do not open a public issue for a suspected vulnerability.

Use GitHub's private vulnerability reporting for this repository, or contact the
repository owner directly through GitHub if private reporting is unavailable.

Include:

- A short description of the issue.
- Steps to reproduce or a minimal proof of concept.
- The affected package, file, or generated application behavior.
- Any known mitigation or patched version.

## Secrets and Generated Files

Do not commit environment-specific Amplify or Cypress files, including:

- `src/amplify/aws-exports.js`
- `amplify/#current-cloud-backend/`
- `amplify/.config/local-*`
- `amplify/backend/amplify-meta.json`
- `cypress-config.json`
- AWS credentials, OAuth tokens, or test-user passwords

These files are intentionally ignored by Git. If a secret is committed by
mistake, revoke or rotate it before removing it from the repository.
