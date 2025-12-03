export interface YoutubeVideoMetadata {
  id: string;
  title: string;
  description: string;
  url: string;
  publishedAt: string;
  channelTitle: string;
  thumbnailUrl: string;
}

export function mockYoutubeVideoMetadata(): YoutubeVideoMetadata[] {
  return [
    {
      id: '123123',
      title: 'MockTitle',
      description: 'MockDescription',
      url: 'MockURl',
      publishedAt: 'MockDate',
      channelTitle: 'MockTitle',
      thumbnailUrl: 'MockUrl',
    },
  ];
}
