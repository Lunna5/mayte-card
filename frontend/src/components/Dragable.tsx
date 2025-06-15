import { isMobile } from '@/utils/constants';
import type { ReactNode } from 'react';
import { Rnd, type Props } from 'react-rnd';

export const WindowDraggable = ({
  children,
  dragHandleClassName,
  ...props
}: {
  children: ReactNode;
  dragHandleClassName?: string;
} & Props) => {
  if (isMobile) {
    return <div>{children}</div>;
  }

  return (
    <Rnd
      dragHandleClassName={dragHandleClassName || 'window-title'}
      bounds='parent'
      default={{ x: 0, y: 0, width: 400, height: 560 }}
      className='sm:scale-100 scale-75'
      disableDragging={isMobile}
      style={{ zIndex: 1000 }}
      scale={isMobile ? 1 : 0.75}
      {...props}
    >
      {children}
    </Rnd>
  );
};
