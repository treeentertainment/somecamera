import path from 'path';
import remarkGithubAdmonitionsToDirectives from 'remark-github-admonitions-to-directives';

export default {
  title: 'SomeCamera',
  url: 'https://camera.treeentertainment.tech',
  baseUrl: '/',
  favicon: 'favicon.ico',
  onBrokenLinks: 'warn',
  markdown: {
    mermaid: true,
    hooks: {
      onBrokenMarkdownLinks: 'warn',
    },
  },
  themes: ['@docusaurus/theme-classic','@docusaurus/theme-mermaid'],
  plugins: [
    [
      '@docusaurus/plugin-sitemap',
      {
        id: 'sitemap-en',
        changefreq: 'weekly',
        priority: 0.5,
      },
    ],
    [
      '@docusaurus/plugin-sitemap',
      {
        id: 'sitemap-kr',
        changefreq: 'weekly',
        priority: 0.5,
      },
    ],
    [
      '@docusaurus/plugin-client-redirects',
      {
        redirects: [
          {
            from: ['/download', '/en/download', '/kr/download'],
            to: 'https://github.com/treeentertainment/somecamera/releases/latest',
          },
          {
            from: ['/github', '/en/github', '/kr/github'],
            to: 'https://github.com/treeentertainment/somecamera',
          },
        ],
      },
    ],
    [
    '@docusaurus/plugin-content-pages',
     {
       id: 'home',
       path: './pages/home',
       routeBasePath: '/',
       remarkPlugins: [remarkGithubAdmonitionsToDirectives],
     },
    ],
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'en',
        path: './pages/en',
        routeBasePath: 'en',
        sidebarPath: require.resolve('./sidebars/sidebars_en.js'),
        remarkPlugins: [remarkGithubAdmonitionsToDirectives],
      },
    ],
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'kr',
        path: './pages/kr',
        routeBasePath: 'kr',
        sidebarPath: require.resolve('./sidebars/sidebars_kr.js'),
        remarkPlugins: [remarkGithubAdmonitionsToDirectives],
      },
    ],
  ],

  themeConfig: {
    navbar: {
      title: 'SomeCamera',
      items: [
        { href: '/github/', label: 'GitHub', position: 'right', target: '_blank' },
        { href: '/download/', label: 'Download', position: 'right', target: '_blank' },
        { to: '/en/intro', label: 'Docs (EN)', position: 'left' },
        { to: '/kr/intro', label: 'Docs (KR)', position: 'left' },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        { label: 'GitHub', href: '/github/', target: '_blank' },
        { label: 'Download', href: '/download/', target: '_blank' },
        { label: 'Docs (EN)', to: '/en/intro' },
        { label: 'Docs (KR)', to: '/kr/intro' },
      ],
    },
  },
};
