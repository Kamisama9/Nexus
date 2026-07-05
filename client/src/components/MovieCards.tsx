import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../../@/components/ui/card";

interface videoName {
  id: number;
  fileName: string;
  overView: string;
  posterPath: string;
}

const MovieCards = ({
  videoFiles,
}: {
  videoFiles: videoName[] | undefined;
}) => {
  return (
    <div className="movie-cards">
      <div className="movie-card grid grid-rows-3 gap-4">
        { videoFiles?.map((video) =>(
          <div className="movie-card-item" key={video.id}>
            <Card>
              <CardHeader>
                <CardTitle>{video.fileName}</CardTitle>
                <CardDescription>{video.overView}</CardDescription>
                <CardAction>Card Action</CardAction>
              </CardHeader>
              <CardContent>
                <p>Card Content</p>
              </CardContent>
            </Card>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MovieCards;
