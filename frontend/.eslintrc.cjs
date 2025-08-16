export default {
  env: {
    browser: true,
    es2021: true
  },
  extends: ['eslint:recommended', 'plugin:vue/vue3-recommended', 'plugin:prettier/recommended'],
  parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module'
  },
  plugins: ['vue'],
  rules: {
    'vue/multi-word-component-names': 'off',
    'no-multiple-empty-lines': ['error', { max: 1, maxEOF: 1 }],
    // 某些地方必须有空行
    'padding-line-between-statements': [
      'error',
      { blankLine: 'always', prev: '*', next: 'return' }, // return 前必须空一行
      { blankLine: 'always', prev: ['const', 'let', 'var'], next: 'return' }, // 变量声明后 return 要空一行
      { blankLine: 'always', prev: '*', next: 'if' }, // if 前空行
      { blankLine: 'always', prev: 'block-like', next: '*' } // 代码块后面空行
    ]
  }
}
