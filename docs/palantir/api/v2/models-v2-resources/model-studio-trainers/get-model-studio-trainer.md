`GET /api/v2/models/modelStudioTrainers/{modelStudioTrainerTrainerId}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Gets details about a specific trainer by its ID and optional version.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-read`.

**OAuth2 scopes**: `api:models-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelStudioTrainerTrainerId` | string | 是 | The Resource Identifier (RID) of a trainer.<br>示例: `ri.models..trainer.autogluon_tabular_regression` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `version` | string | 否 | Specific version of the trainer to retrieve. If not specified, returns the latest version.<br>示例: `0.388.0` |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ModelStudioTrainer**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ModelStudioTrainer` | object | 是 | 示例: `{"outputs":{"model":{"name":"Output model","optional":false,"type":{"type":"model","model":{"modelApiAliases":[{"alias":"input_df","description":"Input dataset"},{"alias":"output_df","description":"Output dataset"}]}}}},"customConfigSchema":{"$schema":"https://json-schema.org/draft/2020-12/schema","title":"AutoGluonTabularRegressionConfig","type":"object","additionalProperties":false,"properties":{"eval_metric":{"title":"Evaluation metric","default":"mean_squared_error","$ref":"#/$defs/RegressionMetricType"},"presets":{"title":"Training presets","default":"medium_quality","$ref":"#/$defs/PresetsType"},"refit_full":{"title":"Refit on full data","description":"After evaluation, refit the best model on the combined training and test data.","type":"boolean","default":false}},"$defs":{"PresetsType":{"title":"PresetsType","description":"Pre-built training presets","oneOf":[{"type":"string","const":"best_quality","description":"Best predictive accuracy, at the cost of training and inference speed."},{"type":"string","const":"medium_quality","description":"Medium predictive accuracy with very fast inference and very fast training time."}]},"RegressionMetricType":{"title":"RegressionMetricType","description":"The metric to optimize for when selecting the best model.","oneOf":[{"type":"string","const":"root_mean_squared_error","description":"Measures the square root of the average squared differences between predicted and actual values."},{"type":"string","const":"mean_squared_error","description":"The average of the squared differences between predicted and actual values."}]}}},"inputs":{"input_df":{"name":"Training dataset","description":"Input dataset for training. If no testing dataset is provided, 20% of this dataset will be held out as a test dataset for evaluation.","optional":false,"type":{"type":"dataset","dataset":{"role":"TRAINING","columnsTypeSpecs":{"target_column":{"name":"Target column","isTarget":true,"allowMultiple":false,"optional":false,"supportedTypes":[{"type":"grouped","grouped":"NUMERIC"}]}}}}},"test_df":{"name":"Test dataset","description":"Input dataset for testing. Used for evaluating the best model only.","optional":true,"type":{"type":"dataset","dataset":{"role":"TEST","columnsTypeSpecs":{}}}}},"name":"AutoGluon Tabular Regression Trainer","description":"Regression with AutoGluon TabularPredictor","experimental":false,"type":"GENERIC","version":"0.388.0","trainerId":"ri.models..trainer.autogluon_tabular_regression"}` |
| `ModelStudioTrainer.trainerId` | string | 是 | The Resource Identifier (RID) of a trainer.<br>示例: `ri.models..trainer.autogluon_tabular_regression` |
| `ModelStudioTrainer.version` | string | 是 | The version of this trainer.<br>示例: `0.388.0` |
| `ModelStudioTrainer.name` | string | 是 | Human-readable name of the trainer.<br>示例: `AutoGluon Tabular Regression Trainer` |
| `ModelStudioTrainer.type` | enum | 是 | The category of machine learning task this trainer is designed to solve.<br>示例: `GENERIC` |
| `ModelStudioTrainer.description` | string | 是 | Description of what this trainer does and its capabilities.<br>示例: `Regression with AutoGluon TabularPredictor` |
| `ModelStudioTrainer.customConfigSchema` | any | 是 | JSON schema defining the custom configuration parameters for this trainer.<br>示例: `{"$schema":"https://json-schema.org/draft/2020-12/schema","title":"AutoGluonTabularRegressionConfig","type":"object","additionalProperties":false,"properties":{"eval_metric":{"title":"Evaluation metric","default":"mean_squared_error","$ref":"#/$defs/RegressionMetricType"},"presets":{"title":"Training presets","default":"medium_quality","$ref":"#/$defs/PresetsType"},"refit_full":{"title":"Refit on full data","description":"After evaluation, refit the best model on the combined training and test data.","type":"boolean","default":false}},"$defs":{"PresetsType":{"title":"PresetsType","description":"Pre-built training presets","oneOf":[{"type":"string","const":"best_quality","description":"Best predictive accuracy, at the cost of training and inference speed."},{"type":"string","const":"medium_quality","description":"Medium predictive accuracy with very fast inference and very fast training time."}]},"RegressionMetricType":{"title":"RegressionMetricType","description":"The metric to optimize for when selecting the best model.","oneOf":[{"type":"string","const":"root_mean_squared_error","description":"Measures the square root of the average squared differences between predicted and actual values."},{"type":"string","const":"mean_squared_error","description":"The average of the squared differences between predicted and actual values."}]}}}` |
| `ModelStudioTrainer.inputs` | any | 是 | Input specifications for this trainer.<br>示例: `{"input_df":{"name":"Training dataset","description":"Input dataset for training. If no testing dataset is provided, 20% of this dataset will be held out as a test dataset for evaluation.","optional":false,"type":{"type":"dataset","dataset":{"role":"TRAINING","columnsTypeSpecs":{"target_column":{"name":"Target column","isTarget":true,"allowMultiple":false,"optional":false,"supportedTypes":[{"type":"grouped","grouped":"NUMERIC"}]}}}}},"test_df":{"name":"Test dataset","description":"Input dataset for testing. Used for evaluating the best model only.","optional":true,"type":{"type":"dataset","dataset":{"role":"TEST","columnsTypeSpecs":{}}}}}` |
| `ModelStudioTrainer.outputs` | any | 是 | Output specifications for this trainer.<br>示例: `{"model":{"name":"Output model","optional":false,"type":{"type":"model","model":{"modelApiAliases":[{"alias":"input_df","description":"Input dataset"},{"alias":"output_df","description":"Output dataset"}]}}}}` |
| `ModelStudioTrainer.experimental` | boolean | 是 | Whether this trainer is experimental and may have breaking changes.<br>示例: `false` |

```json
{
  "outputs": {
    "model": {
      "name": "Output model",
      "optional": false,
      "type": {
        "type": "model",
        "model": {
          "modelApiAliases": [
            {
              "alias": "input_df",
              "description": "Input dataset"
            },
            {
              "alias": "output_df",
              "description": "Output dataset"
            }
          ]
        }
      }
    }
  },
  "customConfigSchema": {
    "$schema": "https://json-schema.org/draft/2020-12/schema",
    "title": "AutoGluonTabularRegressionConfig",
    "type": "object",
    "additionalProperties": false,
    "properties": {
      "eval_metric": {
        "title": "Evaluation metric",
        "default": "mean_squared_error",
        "$ref": "#/$defs/RegressionMetricType"
      },
      "presets": {
        "title": "Training presets",
        "default": "medium_quality",
        "$ref": "#/$defs/PresetsType"
      },
      "refit_full": {
        "title": "Refit on full data",
        "description": "After evaluation, refit the best model on the combined training and test data.",
        "type": "boolean",
        "default": false
      }
    },
    "$defs": {
      "PresetsType": {
        "title": "PresetsType",
        "description": "Pre-built training presets",
        "oneOf": [
          {
            "type": "string",
            "const": "best_quality",
            "description": "Best predictive accuracy, at the cost of training and inference speed."
          },
          {
            "type": "string",
            "const": "medium_quality",
            "description": "Medium predictive accuracy with very fast inference and very fast training time."
          }
        ]
      },
      "RegressionMetricType": {
        "title": "RegressionMetricType",
        "description": "The metric to optimize for when selecting the best model.",
        "oneOf": [
          {
            "type": "string",
            "const": "root_mean_squared_error",
            "description": "Measures the square root of the average squared differences between predicted and actual values."
          },
          {
            "type": "string",
            "const": "mean_squared_error",
            "description": "The average of the squared differences between predicted and actual values."
          }
        ]
      }
    }
  },
  "inputs": {
    "input_df": {
      "name": "Training dataset",
      "description": "Input dataset for training. If no testing dataset is provided, 20% of this dataset will be held out as a test dataset for evaluation.",
      "optional": false,
      "type": {
        "type": "dataset",
        "dataset": {
          "role": "TRAINING",
          "columnsTypeSpecs": {
            "target_column": {
              "name": "Target column",
              "isTarget": true,
              "allowMultiple": false,
              "optional": false,
              "supportedTypes": [
                {
                  "type": "grouped",
                  "grouped": "NUMERIC"
                }
              ]
            }
          }
        }
      }
    },
    "test_df": {
      "name": "Test dataset",
      "description": "Input dataset for testing. Used for evaluating the best model only.",
      "optional": true,
      "type": {
        "type": "dataset",
        "dataset": {
          "role": "TEST",
          "columnsTypeSpecs": {}
        }
      }
    }
  },
  "name": "AutoGluon Tabular Regression Trainer",
  "description": "Regression with AutoGluon TabularPredictor",
  "experimental": false,
  "type": "GENERIC",
  "version": "0.388.0",
  "trainerId": "ri.models..trainer.autogluon_tabular_regression"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `TrainerNotFound` | The specified trainer does not exist. |
| NOT_FOUND | `ModelStudioTrainerNotFound` | The given ModelStudioTrainer could not be found. |
