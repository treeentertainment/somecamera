// docusaurus.config.js
/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'SomeCamera',
  tagline: 'SomeCamera',
  url: 'https://camera.treeentertainment.tech',
  baseUrl: '/',
  onBrokenLinks: 'warn',
  onBrokenMarkdownLinks: 'warn',
  favicon: 'favicon.ico',
  organizationName: 'treeentertainment', // Usually GitHub org/user name.
  projectName: 'SomeCamera', // Usually repo name.
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          editUrl: 'https://github.com/treeentertainment/somecamera/edit/main/docs',
        },
      },
    ],
  ],
  i18n: {
  defaultLocale: 'en',          // 기본 언어
  locales: ['kr', 'en'],        // 지원 언어
  localeConfigs: {
    kr: { label: '한국어' },
    en: { label: 'English' },
  },
},
};

module.exports = config;
