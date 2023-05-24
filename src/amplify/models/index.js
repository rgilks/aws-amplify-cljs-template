// @ts-check
import {initSchema} from '@aws-amplify/datastore'
import {schema} from './schema'

const {Player, Game} = initSchema(schema)

export {Player, Game}
