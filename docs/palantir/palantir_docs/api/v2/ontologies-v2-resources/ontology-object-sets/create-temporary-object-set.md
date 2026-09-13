`POST /api/v2/ontologies/{ontology}/objectSets/createTemporary`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Creates a temporary `ObjectSet` from the given definition. This `ObjectSet` expires after one hour.


Third-party applications using this endpoint via OAuth2 must request the following operation scopes: `api:ontologies-read api:ontologies-write`.

**OAuth2 scopes**: `api:ontologies-read` `api:ontologies-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branch` | string | 否 | The Foundry branch to reference. If not specified, the default branch will be used.<br>Branches are an experimental feature and not all workflows are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The package version of the generated SDK. |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |

## Request body

```json
{
  "objectSet": {
    "type": "base",
    "objectType": "Employee"
  }
}
```

## Response

**CreateTemporaryObjectSetResponseV2**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `CreateTemporaryObjectSetResponseV2` | object | 是 | Success response.<br>示例: `{"objectSetRid":"ri.object-set.main.temporary-object-set.c32ccba5-1a55-4cfe-ad71-160c4c77a053"}` |
| `CreateTemporaryObjectSetResponseV2.objectSetRid` | string | 是 | — |

```json
{
  "objectSetRid": "ri.object-set.main.temporary-object-set.c32ccba5-1a55-4cfe-ad71-160c4c77a053"
}
```
