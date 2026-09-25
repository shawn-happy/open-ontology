`GET /api/v2/ontologies/{ontology}/objects/{objectType}/{primaryKey}/ciphertexts/{property}/decrypt`

Decrypt the value of a ciphertext property.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The API name of the object type. To find the API name, use the **List object types** endpoint or<br>check the **Ontology Manager**.<br>示例: `employee` |
| `primaryKey` | string | 是 | The primary key of the object with the CipherText property.<br>示例: `50030` |
| `property` | string | 是 | The API name of the CipherText property. To find the API name for your CipherText property,<br>check the **Ontology Manager** or use the **Get object type** endpoint.<br>示例: `performance` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branch` | string | 否 | The Foundry branch to read from. If not specified, the default branch will be used.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |

## Response

**DecryptionResult**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `DecryptionResult` | object | 是 | Success response.<br>示例: `{"plaintext":"Jane Doe"}` |
| `DecryptionResult.plaintext` | string | 是 | — |

```json
{
  "plaintext": "Jane Doe"
}
```
