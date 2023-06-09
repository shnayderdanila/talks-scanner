import { createBrowserRouter } from "react-router-dom";
import SignUp from "@root/scenes/SignUp";
import SignIn from "@root/scenes/SignIn";
import IdeaList from "@root/scenes/IdeaList";
import TopicList from "@root/scenes/TopicList";
import Topic from "@root/scenes/Topic";
import Idea from "@root/scenes/Idea";
import Home from "@root/scenes/Home";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
  },
  {
    path: "/signup",
    element: <SignUp />,
  },
  {
    path: "/signin",
    element: <SignIn />,
  },
  {
    path: "/ideas",
    element: <IdeaList />,
  },
  {
    path: "/topics",
    element: <TopicList />,
  },
  {
    path: "/topics/:id",
    element: <Topic />,
  },
  {
    path: "/ideas/:id",
    element: <Idea />,
  },
]);

export default router;
