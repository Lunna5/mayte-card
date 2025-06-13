export const ErrorSpan = ({ error }: { error: string | undefined | null }) => {
    if (!error) return <></>;
    
    return (
        <span className="text-sm">
        {error}
        </span>
    );
}
