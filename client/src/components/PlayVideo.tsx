  import { useParams } from "react-router-dom";

  const PlayVideo = () => {
    const { id } = useParams();
    console.log(id);
    const videoUrl = `http://localhost:8080/api/v1/play/${id}`;
    return (
      <>
        <p>Now Playing video {id}</p>
        <video 
          controls 
          width="800" 
          height="500" 
          autoPlay
        >
          <source src={videoUrl} type="video/mp4" />  // A simple get request
        </video>
      </>
    );
  };

  export default PlayVideo;
