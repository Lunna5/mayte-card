import { LoadingContext } from "@/contexts/LoadingContext";
import { useContext } from "react";

export const useLoading = () => useContext(LoadingContext);