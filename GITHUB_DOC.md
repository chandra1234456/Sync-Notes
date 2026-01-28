# GitHub Release API – Field Documentation

This document describes the structure and meaning of each field returned by the GitHub REST API for a **Release** object.

---

## Release Object (Top-Level Fields)

| Field | Type | Description |
|------|------|-------------|
| `url` | string | API endpoint for this specific release |
| `assets_url` | string | API endpoint to list assets for this release |
| `upload_url` | string | URL template used to upload assets (`{?name,label}`) |
| `html_url` | string | Public GitHub web page for the release |
| `id` | number | Unique numeric ID of the release |
| `node_id` | string | GraphQL node ID for the release |
| `tag_name` | string | Git tag associated with the release (e.g. `v1.1.9`) |
| `target_commitish` | string | Commit SHA or branch the release targets |
| `name` | string | Human-readable release name |
| `draft` | boolean | Indicates if the release is a draft |
| `immutable` | boolean | Indicates if the release cannot be edited |
| `prerelease` | boolean | Indicates if the release is a pre-release |
| `created_at` | string | Timestamp when the release was created (ISO 8601) |
| `updated_at` | string | Timestamp when the release was last updated |
| `published_at` | string | Timestamp when the release was published |
| `tarball_url` | string | API URL to download source as `.tar.gz` |
| `zipball_url` | string | API URL to download source as `.zip` |
| `body` | string | Release notes in Markdown format |

---

## Author Object (`author`)

Information about the user or bot that created the release.

| Field | Type | Description |
|------|------|-------------|
| `login` | string | Username of the release creator |
| `id` | number | Unique numeric user ID |
| `node_id` | string | GraphQL node ID |
| `avatar_url` | string | URL of the user’s avatar |
| `gravatar_id` | string | Legacy Gravatar ID (usually empty) |
| `url` | string | API URL for the user |
| `html_url` | string | Public GitHub profile URL |
| `followers_url` | string | API endpoint for followers |
| `following_url` | string | API endpoint for followed users |
| `gists_url` | string | API endpoint for user gists |
| `starred_url` | string | API endpoint for starred repositories |
| `subscriptions_url` | string | API endpoint for watched repositories |
| `organizations_url` | string | API endpoint for user organizations |
| `repos_url` | string | API endpoint for user repositories |
| `events_url` | string | API endpoint for user events |
| `received_events_url` | string | API endpoint for received events |
| `type` | string | Account type (`User` or `Bot`) |
| `user_view_type` | string | Visibility context (usually `public`) |
| `site_admin` | boolean | Indicates if the user is a GitHub site admin |

---

## Assets Array (`assets[]`)

Files attached to the release.

| Field | Type | Description |
|------|------|-------------|
| `url` | string | API endpoint for the asset |
| `id` | number | Unique numeric asset ID |
| `node_id` | string | GraphQL node ID for the asset |
| `name` | string | File name of the asset |
| `label` | string | Optional display label |
| `uploader` | object | User or bot who uploaded the asset |
| `content_type` | string | MIME type of the asset |
| `state` | string | Upload state (`uploaded`) |
| `size` | number | File size in bytes |
| `digest` | string | Cryptographic hash (SHA-256) |
| `download_count` | number | Number of times the asset was downloaded |
| `created_at` | string | Timestamp when the asset was created |
| `updated_at` | string | Timestamp when the asset was last updated |
| `browser_download_url` | string | Public URL to download the asset |

---

## Asset Uploader Object (`assets[].uploader`)

| Field | Type | Description |
|------|------|-------------|
| `login` | string | Username of the uploader |
| `id` | number | Unique numeric user ID |
| `node_id` | string | GraphQL node ID |
| `avatar_url` | string | URL of the uploader’s avatar |
| `url` | string | API URL for the uploader |
| `html_url` | string | Public GitHub profile URL |
| `type` | string | Account type (`User` or `Bot`) |
| `site_admin` | boolean | Indicates if the uploader is a site admin |

---

## Notes

- Bot accounts (e.g. `github-actions[bot]`) commonly appear as authors or uploaders in CI/CD workflows.
- Asset download URLs are publicly accessible unless the repository is private.
- `digest` can be used to verify file integrity after download.

---

## References

- GitHub REST API – Releases  
  https://docs.github.com/en/rest/releases/releases
